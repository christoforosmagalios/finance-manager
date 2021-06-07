export class ResetPasswordDTO {

    public password: string;
    public confirmPassword: string;
    public changePassword: boolean;
    public resetPasswordId: string;

    constructor() {
        this.changePassword = true;
    }
}