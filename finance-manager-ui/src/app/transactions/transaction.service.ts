import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Constants } from '../shared/constants/constants';

@Injectable({providedIn: 'root'})
export class TransactionService {
    endpoint = Constants.ENTITY.TRANSACTION;

    constructor(private http: HttpClient) {}

    getGroupedExpenses() {
        return this.http.get(Constants.API + '/' + this.endpoint + '/groupedExpenses');
    }
}