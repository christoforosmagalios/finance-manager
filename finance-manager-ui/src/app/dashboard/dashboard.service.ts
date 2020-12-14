import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Constants } from '../shared/constants/constants';

@Injectable({providedIn: 'root'})
export class DashboardService {
    billEndopoint = Constants.ENTITY.BILL;

    constructor(private http: HttpClient) {}

    /**
     * Get the current month's pending amount for the unpaid bills.
     */
    getPending() {
        return this.http.get(Constants.API + '/' + this.billEndopoint + '/pending');
    }
}