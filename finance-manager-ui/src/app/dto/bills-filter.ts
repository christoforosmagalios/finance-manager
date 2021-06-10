import { BillCategoryDTO } from "./bill-category-dto";

export class BillsFilter {

    public code: string;
    public billCategory: BillCategoryDTO;
    public paid: boolean;
    public issueDateFrom: string;
    public issueDateTo: string;
    public dueDateFrom: string;
    public dueDateTo: string;
    public amountFrom: number;
    public amountTo: number;

    constructor() {
        // Do nothing.
    }
}