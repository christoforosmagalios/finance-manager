import { BaseDTO } from './base-dto';
import { TransactionCategoryDTO } from './transaction-category-dto';
import { BillDTO } from './bill-dto';

export class TransactionDTO extends BaseDTO {

    public type: boolean;
    public transactionCategory: TransactionCategoryDTO;
    public date: string;
    public description: string;
    public notes: string;
    public amount: number;
    public bill: BillDTO;

    // To be used only in UI.
    public enableDelete: boolean;

    constructor(date: string) {
        super();
        // Default values.
        this.type = true;
        this.amount = 0;
        this.enableDelete = false;  
        this.bill = new BillDTO(date);    
    }
}