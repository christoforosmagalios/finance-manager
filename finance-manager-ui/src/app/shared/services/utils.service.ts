import { Injectable } from "@angular/core";
import { NgbDate, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from "ngx-toastr";
import { ConfirmationModalComponent } from "../components/confirmation-modal/confirmation-modal.component";
import { Location } from '@angular/common';
import { Constants } from "../constants/constants";

@Injectable({providedIn: 'root'})
export class UtilsService {

    constructor(
        private _modalService: NgbModal,
        private toastr: ToastrService,
        private translateService: TranslateService,
        private location: Location
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
     * @param n The number to be parsed.
     */
    setTwoNumberDecimal(n: string) {
        n = parseFloat(n).toFixed(2);
        return n;
    }

    /**
     * Format the given number to x,xxx.xx.
     * 
     * @param n The number to be formated.
     */
    formatNumber(n: number) {
        return n.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    }

    /**
     * Go to previous state.
     */
    goBack() {
        this.location.back();
    }

    /**
     * Clear any previous validation errors.
     */
    clearErrors(errors: Object) {
        for (let property in errors) {
            errors[property] = null;
        }
    }

    /**
     * Mark the validation errors.
     * 
     * @param errors Object that contains the form elements.
     * @param error  Http Error response.
     */
    markErrors(errors: Object, error: any) {
        if (error && error.errors instanceof Array) {
            for (let e of error.errors) {
                errors[e.field] = e.defaultMessage;
            }
        }
    }

    /**
     * Handle date selection.
     * 
     * @param date The new date.
     */
    onDateSelection(date: NgbDate) {
        return this.getDate(new Date(date.year + "-" + date.month + "-" + date.day));
    }

    /**
     * Check if the given date is before or equal to today.
     * 
     * @param date String date 
     */
    dateIfBeforeToday(date: string) {
        return new Date(date).setHours(0,0,0,0) < new Date().setHours(0,0,0,0);
    }

    /**
     * Extract the translated month word from the given date.
     * 
     * @param date The date.
     * @returns The translated name of the month.
     */
    getMonth(date: Date) {
        let monthIndex = new Date(date).getMonth();
        return this.translate(Constants.MONTHS[monthIndex]);
    }

    /**
     * Extract the translated month word from the given month index.
     * 
     * @param index The month index.
     * @returns The translated name of the month.
     */
    getMonthByIndex(index: number) {
        return this.translate(Constants.MONTHS[index]);
    }

    /**
     * Get the date today as a string.
     * Format: yyyy-mm-dd
     * 
     * @returns Date as string.
     */
    getNewDate() {
        return this.getDate(new Date());
    }

    /**
     * Convert the given date to a string.
     * Format: yyyy-mm-dd
     * 
     * @returns Date as string.
     */
    getDate(d: Date) {
        return d.getFullYear() + "-" + this.addLeadingZero(d.getMonth() + 1) + "-" + this.addLeadingZero(d.getDate());
    }

    /**
     * Add a leading zero to the given number.
     * 
     * @param n The number.
     * @returns The number with a leading zero needed.
     */
    addLeadingZero(n: number) {
        return (n < 10) ? ("0" + n) : n;
    }
}