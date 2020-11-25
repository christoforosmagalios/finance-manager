import { Injectable } from "@angular/core";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { ToastrService } from "ngx-toastr";
import { ConfirmationModalComponent } from "../components/confirmation-modal/confirmation-modal.component";
import { Messages } from "../constants/messages";

@Injectable({providedIn: 'root'})
export class UtilsService {

    constructor(
        private _modalService: NgbModal,
        private toastr: ToastrService
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
        this.toastr.success(text, Messages.SUCCESS);
    }

    /**
     * Show an error toaster.
     * 
     * @param text The message.
     */
    showError(text: string) {
        this.toastr.error(text, Messages.ERROR);
    }

    /**
     * Show a warning toaster.
     * 
     * @param text The message.
     */
    showWarning(text: string) {
        this.toastr.warning(text, Messages.WARNING);
    }

    /**
     * Show an info toaster.
     * 
     * @param text The message.
     */
    showInfo(text: string) {
        this.toastr.info(text, Messages.INFO);
    }

}