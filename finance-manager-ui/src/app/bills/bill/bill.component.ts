import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { NgbDate, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { BillCategoryDTO } from 'src/app/dto/bill-category-dto';
import { BillDTO } from 'src/app/dto/bill-dto';
import { Constants } from 'src/app/shared/constants/constants';
import { CRUDService } from 'src/app/shared/services/crud.service';
import { UtilsService } from 'src/app/shared/services/utils.service';
import { NgbDateFRParserFormatter } from 'src/app/shared/services/datepicker.formatter.service';
import { BillDates } from 'src/app/dto/bill-dates-dto';
import { BillService } from '../bill.service';
import { AddressDTO } from 'src/app/dto/address-dto';

@Component({
  selector: 'app-bill',
  templateUrl: './bill.component.html',
  styleUrls: ['./bill.component.css'],
  providers: [{provide: NgbDateParserFormatter, useClass: NgbDateFRParserFormatter}]
})
export class BillComponent implements OnInit {

  // Reference to the input file.
  @ViewChild('uploader') uploader: ElementRef;
  // The id of the bill to be edited.
  id: string;
  // True if edit mode, false otherwise.
  editMode = false;
  // The bill.
  bill = new BillDTO(this.utils.getNewDate());
  // The original edited bill.
  originalBill = new BillDTO(this.utils.getNewDate());
  // Bill dates.
  dates = new BillDates();
  // List of bill categories.
  billCategories: Array<BillCategoryDTO>;
  // List of bill addresses.
  addresses: Array<AddressDTO>;
  // Image location prefix.
  imagePathPrefix = Constants.API+Constants.IMAGES+"/";
  // Error object.
  errors = {
    consumptionFrom: null,
    consumptionTo: null,
    notes: null,
    description: null,
    amount: null,
    billCategory: null,
    issuedOn: null,
    dueDate: null,
    paid: null,
    actualBill: null
  };

  constructor(
    private route: ActivatedRoute,
    public formatter: NgbDateParserFormatter,
    private crudService: CRUDService,
    public utils: UtilsService,
    private router: Router,
    private billService: BillService
  ) { }

  ngOnInit(): void {
    // Get the params.
    this.route.params.subscribe((params: Params) => {
      this.id = params['id'];
      // Initialize the form.
      this.initForm();
    });
  }

  /**
   * Initialize the bill Form.
   */
  private initForm() {
    // Find all the categories.
    this.crudService.findAll(Constants.ENTITY.BILL_CATEGORY)
    .subscribe((billCategories: Array<BillCategoryDTO>) => {
      this.billCategories = billCategories;
    });

    // Find all the addresses.
    this.crudService.findAll(Constants.ENTITY.ADDRESS)
    .subscribe((addresses: Array<AddressDTO>) => {
      this.addresses = addresses;
    });

    // In case of edit mode fetch the bill from the database.
    if (!this.id) {
      this.editMode = true;
    } else {
      this.crudService.findOne(Constants.ENTITY.BILL, this.id)
      .subscribe((transaction: BillDTO) => {
        this.bill = transaction;
        this.initDates();
        this.originalBill = {...this.bill};
      });
    }
  }

  /**
   * Initialize the bill dates.
   */
  private initDates() {
    for (let key in this.dates) {
      this.setDate(key);
    }
  }

/**
   * Set the given Date.
   * 
   * @param date A Date.
   */
  private setDate(key: string) {
    if (this.bill[key]) {
      let date = new Date(this.bill[key]);
      this.dates[key] = new NgbDate(date.getFullYear(), date.getMonth() + 1,  date.getDate());
    }
    
  }

  /**
   * Clear the paid on date if the Paid radio is false.
   */
  handleIsPaid() {
    if (!this.bill.paid) {
      this.bill.paidOn = null;
      this.dates.paidOn = null;
    }
  }

  /**
   * Submit the bill form.
   * 
   * @param form The Form.
   */
  onSubmit(form: NgForm) {
    // Clear the form errors.
    this.utils.clearErrors(this.errors);
    // Save the bill.
    this.billService.saveBill(this.bill).subscribe(result => {
      // Show success notification.
      this.utils.showSuccess("bill.saved.successfully");
      // Navigate to Bills view.
      this.router.navigate(['/bills']);
    }, error => {
      // Mark the validation errors.
      this.utils.markErrors(this.errors, error);
    });
  }

  /**
   * Navigate to previous state.
   */
  onCancel() {
    if(this.editMode && this.id) {
      this.bill = {...this.originalBill};
      this.editMode = false;
      this.utils.clearErrors(this.errors);
    } else {
      this.utils.goBack();
    }
  }

  /**
   * Upload the image.
   * 
   * @param files The file list of the file input.
   */
  upload(files: FileList) {
    if(files.length > 0) {
      this.billService.upload(files.item(0)).subscribe((base64 : string) => {
        this.bill.base64 = base64;
      });
    }
  }

  /**
   * Clear the bill image.
   */
  clearImage() {
    // If the bill has an image path delete it from the file system.
    if (this.bill.imgPath) {
      this.billService.deleteImage(this.bill.id).subscribe(res => {
        this.bill.imgPath = null;
      });
    } else {
      this.bill.base64 = null;
      this.uploader.nativeElement.value = "";
    }
  }
}
