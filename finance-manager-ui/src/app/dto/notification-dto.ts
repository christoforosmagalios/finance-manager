import { BaseDTO } from './base-dto';

export class NotificationDTO extends BaseDTO {

    public description: string;
    public icon: string;
    public url: string;
    public seen: boolean;
    public parameters: any;

    constructor() {
        super();
    }
}