import { BaseDTO } from './base-dto';
import { TransactionCategoryDTO } from './transaction-category-dto';
import { BillDTO } from './bill-dto';

export class TransactionDTO extends BaseDTO {

    public type: boolean;
    public transactionCategory: TransactionCategoryDTO;
    public date: Date;
    public description: string;
    public notes: string;
    public amount: number;
    public bill: BillDTO;

    // To be used only in UI.
    public enableDelete: boolean;

    constructor() {
        super();
        // Default values.
        this.type = true;
        this.amount = 0;
        this.enableDelete = false;  
        this.bill = new BillDTO();    
    }
}