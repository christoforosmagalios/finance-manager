import { BaseDTO } from './base-dto';

export class TransactionDTO extends BaseDTO {

    public type: boolean;
    public category: string;
    public date: Date;
    public description: string;
    public notes: string;
    public amount: number;

    constructor() {
        super();
        // Default type Expense.
        this.type = true;        
    }
}