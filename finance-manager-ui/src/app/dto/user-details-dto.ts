import { UserDTO } from "./user-dto";

export class UserDetailsDTO extends UserDTO {

    public password: string;
    public confirmPassword: string;
    public changePassword: boolean;

    constructor() {
        super();
        this.changePassword = false;
    }
}