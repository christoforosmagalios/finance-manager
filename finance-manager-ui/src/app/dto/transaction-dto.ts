import { BaseDTO } from './base-dto';

export class TransactionDTO extends BaseDTO {

    public type: number;
    public category: string;
    public date: Date;
    public description: string;
    public notes: string;
    public amount: number;

    constructor() {
        super();
        this.type = 0;
        this.date = new Date();
    }
}