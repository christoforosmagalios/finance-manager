export class AuthDTO {

    public fullname: string;
    public token: string;

    constructor(fullname: string, token: string) {
        this.fullname = fullname;
        this.token = token;
    }
}