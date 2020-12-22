import { BaseDTO } from './base-dto';

export class AddressDTO extends BaseDTO {

    public description: string;

    // To be used only in UI.
    public enableDelete: boolean;
    public enableEdit: boolean;

    constructor() {
        super();
        this.enableDelete = false;
        this.enableEdit = false;
    }
}