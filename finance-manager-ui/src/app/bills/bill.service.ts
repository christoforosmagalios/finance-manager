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
     * Upload the given file.
     * 
     * @param file The file to be uploaded.
     */
    upload(file: File) {
        const formData: FormData = new FormData();
        formData.append('file', file, file.name);
        return this.http.post(Constants.API + '/' + this.endpoint + '/upload', formData);
    }
}