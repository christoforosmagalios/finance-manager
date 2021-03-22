import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Constants } from '../../shared/constants/constants';

@Injectable({ providedIn: 'root' })
export class DataService {
    endpoint = Constants.ENTITY.ELASTICSEARCH;

    constructor(private http: HttpClient) { }

    /**
     * Perform a full reindex.
     */
    reindex() {
        return this.http.post(Constants.API + '/' + this.endpoint + '/reindex', null);
    }

    /**
     * Search for Bills and Transactions that contain the given text in their description or notes.
     * 
     * @param text The text to search with.
     */
    search(text: string) {
        return this.http.get(Constants.API + '/' + this.endpoint + '/search/' + text);
    }
}