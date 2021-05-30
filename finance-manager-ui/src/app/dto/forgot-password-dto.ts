export class ForgotPasswordDTO {

    public email: string;
    public language: string;

    constructor(email: string, language: string) {
        this.email = email;
        this.language = language;
    }
}