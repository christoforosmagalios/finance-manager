import { TransactionCategoryDTO } from "./transaction-category-dto";

export class TransactionsFilter {

    public type: boolean;
    public dateFrom: Date;
    public dateTo: Date;
    public amountFrom: number;
    public amountTo: number;
    public billCode: string;
    public transactionCategory: TransactionCategoryDTO;

    constructor() {
    }
}