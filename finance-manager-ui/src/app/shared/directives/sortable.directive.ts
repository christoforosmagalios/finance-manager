import { Directive, ElementRef, HostListener, OnInit, Renderer2, Output, EventEmitter, Input, SimpleChanges } from '@angular/core';
import { Constants } from '../constants/constants';

@Directive({
    selector: '[sortable]'
})
export class SortableDirective implements OnInit {

    // The Event emitter.
    @Output() ngModelChange: EventEmitter<any> = new EventEmitter();
    // The name of the column.
    @Input() columnName: string;
    // The active sort column.
    @Input() active: string;

    constructor(
        private element: ElementRef,
        private renderer: Renderer2) {
    }

    ngOnInit() {
    }

    ngOnChanges(changes: SimpleChanges) {
        // If another column is active, remove the CSS classes from the current one.
        if (changes.active && this.active !== this.columnName) {
            this.renderer.removeClass(this.element.nativeElement, Constants.ORDER.ASC);
            this.renderer.removeClass(this.element.nativeElement, Constants.ORDER.DESC);
        }
    }

    @HostListener('click') onClick() {
        // Update the column sort name and direction.
        if (this.element.nativeElement.classList.contains(Constants.ORDER.DESC)) {
            // Set sort order to ASC.
            this.update(Constants.ORDER.ASC, Constants.ORDER.DESC, Constants.ORDER.ASC, this.columnName);
        } else if (this.element.nativeElement.classList.contains(Constants.ORDER.ASC)) {
            // Remove sort.
            this.update('', Constants.ORDER.ASC, '', '');
        } else if (!this.element.nativeElement.classList.contains(Constants.ORDER.DESC)
            && !this.element.nativeElement.classList.contains(Constants.ORDER.ASC)) {
            // Set sort order to DESC.
            this.update(Constants.ORDER.DESC, '', Constants.ORDER.DESC, this.columnName);
        }
    }

    /**
     * Update the classes of the element, and emit a new event that contains the
     * new order.
     * 
     * @param addClass CSS class to be added.
     * @param removeClass CSS class to be removed.
     * @param direction The new Sort direction.
     * @param name The new Sort name.
     */
    update(addClass: string, removeClass: string, direction: string, name: string) {
        // Remove the given CSS class.
        removeClass ? this.renderer.removeClass(this.element.nativeElement, removeClass) : null;
        // Add the given CSS class.
        addClass ? this.renderer.addClass(this.element.nativeElement, addClass) : null;
        // Emit the event.
        this.ngModelChange.emit({direction: direction, name: name});
    }
}