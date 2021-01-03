import { Component, OnInit } from '@angular/core';
import { BillService } from 'src/app/bills/bill.service';
import { BillDTO } from 'src/app/dto/bill-dto';
import { UtilsService } from 'src/app/shared/services/utils.service';

@Component({
  selector: 'app-bills-expire-soon',
  templateUrl: './bills-expire-soon.component.html',
  styleUrls: ['./bills-expire-soon.component.css']
})
export class BillsExpireSoonComponent implements OnInit {

  bills: Array<BillDTO>;

  constructor(
    private billService: BillService,
    public utils: UtilsService
  ) { }

  ngOnInit(): void {
    console.log("sadsd");
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
    this.billService.setToPaid(id).subscribe(res => {
      this.utils.showSuccess("set.to.paid.successfully");
      this.findBills();
    });
  }

}
