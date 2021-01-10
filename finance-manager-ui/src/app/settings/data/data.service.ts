import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Constants } from '../../shared/constants/constants';

@Injectable({providedIn: 'root'})
export class DataService {

    constructor(private http: HttpClient) {}

    reindex() {
        return this.http.post(Constants.API + '/elasticsearch/reindex', null);
    }
}