import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable({providedIn: 'root'})
export class LoaderService {
    isLoading: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
    /**
     * Show Loader.
     */
    show() {
        this.isLoading.next(true);
    }

    /**
     * Hide Loader.
     */
    hide() {
        this.isLoading.next(false);
    }
}