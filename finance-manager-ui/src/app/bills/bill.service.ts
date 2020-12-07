import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { BaseDTO } from '../dto/base-dto';
import { BillDTO } from '../dto/bill-dto';
import { PageDTO } from '../dto/page-dto';
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
     * Find bills based on the given pagination parameters.
     * 
     * @param size Page size.
     * @param page The number of the page.
     * @param sort Sort column.
     * @param direction Sort direction. 
     */
    findAllPaginated(size: number, page: number, sort: string, direction: string) {
        return this.http.get<PageDTO<BaseDTO>>(Constants.API + '/bill/paginatedBills?size=' + size + '&page=' + page + '&sort=' + sort + ',' + direction);
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