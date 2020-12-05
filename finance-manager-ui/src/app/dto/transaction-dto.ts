import { BaseDTO } from './base-dto';
import { CategoryDTO } from './category-dto';

export class TransactionDTO extends BaseDTO {

    public type: boolean;
    public category: CategoryDTO;
    public date: Date;
    public description: string;
    public notes: string;
    public amount: number;

    // To be used only in UI.
    public enableDelete: boolean;

    constructor() {
        super();
        // Default values.
        this.type = true;
        this.amount = 0;
        this.enableDelete = false;      
    }
}