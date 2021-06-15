import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { BillService } from 'src/app/bills/bill.service';
import { BillDTO } from 'src/app/dto/bill-dto';
import { LoaderService } from 'src/app/shared/components/loader/loader.service';
import { UtilsService } from 'src/app/shared/services/utils.service';

@Component({
  selector: 'app-bills-expire-soon',
  templateUrl: './bills-expire-soon.component.html'
})
export class BillsExpireSoonComponent implements OnInit {

  // Event to be sent to the parent, in order to update the all the other child components.
  @Output() billPaid: EventEmitter<string> = new EventEmitter<string>();
  // List of bills that expire soon.
  bills: Array<BillDTO>;

  constructor(
    private billService: BillService,
    public utils: UtilsService,
    private loader: LoaderService
  ) { }

  ngOnInit(): void {
    this.findBills();
  }

  /**
   * Find the bills that expire soon.
   */
  private findBills() {
    this.billService.expireSoon().subscribe((bills: Array<BillDTO>) => {
      this.bills = bills;
    });
  }

  /**
   * Set bill to paid.
   * 
   * @param id The id of the Bill to be paid.
   */
  setToPaid(id: string) {
    this.loader.show();
    this.billService.setToPaid(id).subscribe(res => {
      this.utils.showSuccess("set.to.paid.successfully");
      // Send event to the parent.
      this.billPaid.emit();
      this.loader.hide();
      this.findBills();
    });
  }

}
