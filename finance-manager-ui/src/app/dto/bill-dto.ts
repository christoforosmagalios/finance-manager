import { AddressDTO } from './address-dto';
import { BaseDTO } from './base-dto';
import { BillCategoryDTO } from './bill-category-dto';

export class BillDTO extends BaseDTO {

    public billCategory: BillCategoryDTO;
    public code: string;
    public issuedOn: string;
    public dueDate: string;
    public paidOn: string;
    public consumptionFrom: string;
    public consumptionTo: string;
    public description: string;
    public notes: string;
    public amount: number;
    public unpaidAmount: number;
    public paid: boolean;
    public actualBill: boolean;
    public imgPath: string;
    public base64: string;
    public address: AddressDTO;

    // To be used only in UI.
    public enableDelete: boolean;
    public enablePay: boolean;

    constructor(date: string) {
        super();
        // Default values.
        this.paid = false;  
        this.actualBill = false;
        this.enableDelete = false;
        this.enablePay = false;
        this.issuedOn = date;
        this.dueDate = date;
        this.consumptionFrom = date;
        this.consumptionTo = date;
        this.amount = 0;
        this.unpaidAmount = 0;
    }
}