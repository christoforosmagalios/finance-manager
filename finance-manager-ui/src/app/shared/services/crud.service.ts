import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { BaseDTO } from 'src/app/dto/base-dto';
import { Constants } from 'src/app/shared/constants/constants';

@Injectable({providedIn: 'root'})
export class CRUDService {

    constructor(private http: HttpClient) { }

    /**
     * Find all the requested entities.
     * 
     * @return entity The entity name.
     */
    findAll(entity: string) {
        return this.http.get<Array<BaseDTO>>(Constants.API + '/' + entity +'/');
    }

    /**
     * Save the given entity.
     * 
     * @return entity The entity name.
     * @param transaction The entity to be saved.
     */
    save(entity:string, baseDTO: BaseDTO) {
        return this.http.post<BaseDTO>(Constants.API + '/' + entity, baseDTO);
    }

    /**
     * Find the entity with the given id.
     * 
     * @return entity The entity name.
     * @param id The id of the entity.
     */
    findOne(entity:string, id: string) {
        return this.http.get<BaseDTO>(Constants.API + '/' + entity + '/' + id);
    }

}