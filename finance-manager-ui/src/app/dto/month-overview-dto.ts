export class MonthOverviewDTO {

    public month: number;
    public year: number;

    constructor() {
        this.month = new Date().getMonth();
        this.year = new Date().getFullYear();
    }
}