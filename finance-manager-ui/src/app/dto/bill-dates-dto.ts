import { NgbDate } from '@ng-bootstrap/ng-bootstrap';

export class BillDates {

    public issuedOn: NgbDate;
    public dueDate: NgbDate;
    public paidOn: NgbDate;
    public consumptionFrom: NgbDate;
    public consumptionTo: NgbDate;

    constructor() {
        // Default values.
        let date = new Date();
        let ngbDdate = new NgbDate(date.getFullYear(), date.getMonth() + 1,  date.getDate())
        this.issuedOn = ngbDdate;
        this.dueDate = ngbDdate;
        this.paidOn = null;
        this.consumptionFrom = ngbDdate;
        this.consumptionTo = ngbDdate;
    }
}