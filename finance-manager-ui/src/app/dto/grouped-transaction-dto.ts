import { GroupedTransactionInfoDTO } from "./grouped-transaction-info-dto";

export class GroupedTransactionDTO {

    public from: Date;
    public to: Date;
    public transactions: Array<GroupedTransactionInfoDTO>;

    constructor() {
    }
}