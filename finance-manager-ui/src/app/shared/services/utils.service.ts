import { Injectable } from "@angular/core";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from "ngx-toastr";
import { ConfirmationModalComponent } from "../components/confirmation-modal/confirmation-modal.component";

@Injectable({providedIn: 'root'})
export class UtilsService {

    constructor(
        private _modalService: NgbModal,
        private toastr: ToastrService,
        private translateService: TranslateService
    ) {}

    /**
     * Open a confirmation modal with the given parameters and return
     * its instance.
     * 
     * @param title The modal title.
     * @param description The modal text content.
     */
    confirmation(title: string, description: string) {
        // Open the confirmation modal.
        const modal = this._modalService.open(ConfirmationModalComponent);
        // Pass the given parameters to the modal.
        modal.componentInstance.title = title;
        modal.componentInstance.description = description;
        // Return the modal instance.
        return modal;
    }

    /**
     * Show a success toaster.
     * 
     * @param text The message.
     */
    showSuccess(text: string) {
        this.toastr.success(
            this.translate(text), 
            this.translate("success"));
    }

    /**
     * Show an error toaster.
     * 
     * @param text The message.
     */
    showError(text: string) {
        this.toastr.error(
            this.translateWithDefault(text, "generic.error"), 
            this.translate("error"));
    }

    /**
     * Show a warning toaster.
     * 
     * @param text The message.
     */
    showWarning(text: string) {
        this.toastr.warning(
            this.translate(text), 
            this.translate("warning"));
    }

    /**
     * Show an info toaster.
     * 
     * @param text The message.
     */
    showInfo(text: string) {
        this.toastr.info(
            this.translate(text), 
            this.translate("info"));
    }

    /**
     * Translate the given message.
     * 
     * @param message The message to be translated.
     */
    translate(message: string) {
        return this.translateWithDefault(message, message);
    }

    /**
     * Translate the given message. If the message has no translation,
     * translate the given default message.
     * 
     * @param message The message to be translated.
     * @param defaultMessage The default message to be translated.
     */
    private translateWithDefault(message: string, defaultMessage: string) {
        let translated = this.translateService.instant(message);
        if (translated === message) {
            return this.translateService.instant(defaultMessage);
        }
        return translated;
    }

    /**
     * Compare the given object by their ids.
     * 
     * @param obj1 Object to be compared.
     * @param obj2 Object to be compared.
     */
    compareById(obj1, obj2) {
        return obj1 && obj2 && obj1.id == obj2.id;
    }

    /**
     * Set two decimals to the given target.
     * 
     * @param $event The number to be parsed.
     */
    setTwoNumberDecimal($event) {
        $event.target.value = parseFloat($event.target.value).toFixed(2);
    }

}