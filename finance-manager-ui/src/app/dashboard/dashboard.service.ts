import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Constants } from '../shared/constants/constants';

@Injectable({providedIn: 'root'})
export class DashboardService {
    billEndopoint = Constants.ENTITY.BILL;
    transactionEndopoint = Constants.ENTITY.TRANSACTION;

    constructor(private http: HttpClient) {}

    /**
     * Get the current month's pending amount for the unpaid bills.
     */
    getPending() {
        return this.http.get<number>(Constants.API + '/' + this.billEndopoint + '/pending');
    }

    /**
     * Get the current month's transaction amount based on the given type.
     * 
     * @param month The month.
     * @param year The year.
     */
    getMonthlyTransactionAmount(month: number, year: number, type: boolean) {
        return this.http.get<number>(Constants.API + '/' + this.transactionEndopoint + '/monthlyTransactionsAmount/' + month + '/' + year + '/' + type);
    }

    /**
     * Get the annual balance.
     */
    getAnnualBalance() {
        return this.http.get<number>(Constants.API + '/' + this.transactionEndopoint + '/balance');
    }
}