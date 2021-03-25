import { Pipe, PipeTransform } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";

@Pipe({ 
    name: 'timeAgo',
    pure: false 
})
export class TimeAgoPipe implements PipeTransform {
    // Array with time units.
    times = [
        {
            unit: "day",
            seconds: 86400
        }, {
            unit: "hour",
            seconds: 3600
        }, {
            unit: "minute",
            seconds: 60
        }
    ];

    constructor(private translateService: TranslateService) { }

    /**
     * Calculate and translate a string, based on the given Date i.e. "5 minutes ago".
     * 
     * @param value The Date value.
     * @returns The calculated and translated value.
     */
    transform(value: string): string {
        // Calculate the seconds from the given date until now.
        let seconds = Math.floor((+new Date() - +new Date(value)) / 1000);
        // In case the seconds are less than 60, return the translated word "now".
        if (seconds < 60) {
            return this.translateService.instant("now");
        }

        // Iterate though the defined time units.
        for (let i = 0; i < this.times.length; i++) {
            let counter = Math.floor(seconds / this.times[i].seconds);
            if (counter > 0) {
                if (counter === 1) {
                    let unit = this.translateService.instant(this.times[i].unit);
                    return this.translateService.instant("notification.time.ago", { time: counter + " " + unit})
                } else {
                    let unit = this.translateService.instant(this.times[i].unit + "s");
                    return this.translateService.instant("notification.time.ago", { time: counter + " " + unit})
                }
            }
        }
    }

}