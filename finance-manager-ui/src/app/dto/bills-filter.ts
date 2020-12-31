import { BillCategoryDTO } from "./bill-category-dto";

export class BillsFilter {

    public code: string;
    public billCategory: BillCategoryDTO;
    public paid: boolean;
    public issueDateFrom: Date;
    public issueDateTo: Date;
    public dueDateFrom: Date;
    public dueDateTo: Date;
    public amountFrom: number;
    public amountTo: number;

    constructor() {
    }
}