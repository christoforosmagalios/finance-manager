import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { ChartDataSets } from 'chart.js';
import { Constants } from '../shared/constants/constants';

@Injectable({providedIn: 'root'})
export class TransactionService {
    endpoint = Constants.ENTITY.TRANSACTION;

    constructor(private http: HttpClient) {}

    getGroupedExpenses(month: number, year: number) {
        return this.http.get(Constants.API + '/' + this.endpoint + '/groupedExpenses/' + month + '/' + year);
    }

    getTransactionsPerDay(month: number, year: number) {
        return this.http.get(Constants.API + '/' + this.endpoint + '/transactionsPerDay/' + month + '/' + year);
    }

    /**
     * Get the total number of transactions for the logged in user.
     */
    getTotalNumberOfTransactions() {
        return this.http.get<number>(Constants.API + '/' + this.endpoint + '/totalNumberOfTransactions');
    }
    
    getAnnualTransactionsGroupedByMonth(year: number) {
        return this.http.get<ChartDataSets[]>(Constants.API + '/' + this.endpoint + '/annualTransactionsByMonth/' + year);
    }
}