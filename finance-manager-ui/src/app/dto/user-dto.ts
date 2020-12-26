import { BaseDTO } from "./base-dto";

export class UserDTO extends BaseDTO {

    public username: string;
    public firstName: string;
    public lastName: string;
    public email: string;

    constructor() {
        super();
    }
}