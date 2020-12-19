import { AddressDTO } from './address-dto';
import { BaseDTO } from './base-dto';
import { BillCategoryDTO } from './bill-category-dto';

export class BillDTO extends BaseDTO {

    public billCategory: BillCategoryDTO;
    public code: string;
    public issuedOn: Date;
    public dueDate: Date;
    public paidOn: Date;
    public consumptionFrom: Date;
    public consumptionTo: Date;
    public description: string;
    public notes: string;
    public amount: number;
    public unpaidAmount: number;
    public paid: boolean;
    public actualBill: boolean;
    public imgPath: string;
    public imgType: string;
    public base64: string;
    public address: AddressDTO;

    // To be used only in UI.
    public enableDelete: boolean;

    constructor() {
        super();
        // Default values.
        this.paid = false;  
        this.actualBill = false;
        this.enableDelete = false;
        this.issuedOn = new Date();
        this.dueDate = new Date();
        this.consumptionFrom = new Date();
        this.consumptionTo = new Date();
        this.amount = 0;
        this.unpaidAmount = 0;
    }
}