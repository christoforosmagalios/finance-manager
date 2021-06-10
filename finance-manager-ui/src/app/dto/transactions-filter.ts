import { TransactionCategoryDTO } from "./transaction-category-dto";

export class TransactionsFilter {

    public type: boolean;
    public dateFrom: string;
    public dateTo: string;
    public amountFrom: number;
    public amountTo: number;
    public billCode: string;
    public transactionCategory: TransactionCategoryDTO;

    constructor() {
        // Do nothing.
    }
}