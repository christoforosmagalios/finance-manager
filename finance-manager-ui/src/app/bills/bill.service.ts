import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { BillDTO } from '../dto/bill-dto';
import { Constants } from '../shared/constants/constants';

@Injectable({providedIn: 'root'})
export class BillService {
    endpoint = Constants.ENTITY.BILL;

    constructor(private http: HttpClient) {}

    /**
     * Save the given bill.
     * 
     * @param billDTO The bill DTO to be saved.
     */
    saveBill(billDTO: BillDTO) {
        return this.http.post<BillDTO>(Constants.API + '/' + this.endpoint + '/save', billDTO);
    }

    /**
     * Find bills whose password starts with the given text.
     * 
     * @param text Text to search with. 
     */
    findByCode(text: string) {
        return this.http.get(Constants.API + '/' + this.endpoint + '/findByCode/' + text);
    }

    /**
     * Upload the given file.
     * 
     * @param file The file to be uploaded.
     */
    upload(file: File) {
        const formData: FormData = new FormData();
        formData.append('file', file, file.name);
        return this.http.post(Constants.API + '/' + this.endpoint + '/upload', formData);
    }

    /**
     * Find the bills that expire soon.
     */
    expireSoon() {
        return this.http.get(Constants.API + '/' + this.endpoint + '/expireSoon');
    }

    /**
     * Set bill to paid.
     * 
     * @param bill The id of the Bill to be paid.
     */
    setToPaid(id: string) {
        return this.http.post(Constants.API + '/' + this.endpoint + '/setToPaid', id);
    }
}