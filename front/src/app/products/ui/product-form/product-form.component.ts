
import {Component, computed, EventEmitter, input, Output, ViewEncapsulation} from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Product } from "app/products/data-access/product.model";
import { SelectItem } from "primeng/api";
import {Button} from "primeng/button";
import {DropdownModule} from "primeng/dropdown";
import {RatingModule} from "primeng/rating";
import {ChipsModule} from "primeng/chips";
import {InputTextareaModule} from "primeng/inputtextarea";
import {PaginatorModule} from "primeng/paginator";

@Component({
  selector: "app-product-form",
  template: `
    <form #form="ngForm" (ngSubmit)="onSave()" enctype="multipart/form-data">
      <div class="form-field">
        <label for="name">Nom</label>
        <input pInputText
               type="text"
               id="name"
               name="name"
               [(ngModel)]="editedProduct().name"
               required>
      </div>

      <div class="form-field">
        <label for="code">Code</label>
        <input pInputText
               type="text"
               id="code"
               name="code"
               [(ngModel)]="editedProduct().code"
               required>
      </div>

      <div class="form-field">
        <label for="price">Prix</label>
        <p-inputNumber
          [(ngModel)]="editedProduct().price"
          name="price"
          mode="decimal"
          required/>
      </div>

      <div class="form-field">
        <label for="quantity">Quantité</label>
        <p-inputNumber
          [(ngModel)]="editedProduct().quantity"
          name="quantity"
          required/>
      </div>

      <div class="form-field">
        <label for="description">Description</label>
        <textarea pInputTextarea
                  id="description"
                  name="description"
                  rows="5"
                  cols="30"
                  [(ngModel)]="editedProduct().description"
                  required>
                </textarea>
      </div>

      <div class="form-field">
        <label for="internalReference">Référence interne</label>
        <input pInputText
               type="text"
               id="internalReference"
               name="internalReference"
               [(ngModel)]="editedProduct().internalReference"
               required>
      </div>

      <div class="form-field">
        <label for="rating">Note</label>
        <p-rating
          [(ngModel)]="editedProduct().rating"
          name="rating"
          [stars]="5"
          required>
        </p-rating>
      </div>

      <div class="form-field">
        <label for="inventoryStatus">Statut du stock</label>
        <p-dropdown
          [options]="inventoryStatusOptions"
          [(ngModel)]="editedProduct().inventoryStatus"
          name="inventoryStatus"
          required
          appendTo="body"
        />
      </div>

      <div class="form-field">
        <label for="category">Catégorie</label>
        <p-dropdown
          [options]="categories"
          [(ngModel)]="editedProduct().categoryId"
          name="categoryId"
          required
          appendTo="body"
        />
      </div>

      <div class="form-field">
        <label for="image">Image</label>
        <input type="file"
               (change)="onFileSelected($event)"
               accept="image/*"
               [required]="isCreation">
      </div>

      <div class="flex justify-content-between">
        <p-button type="button" (click)="onCancel()" label="Annuler" severity="help"/>
        <p-button type="submit" [disabled]="!form.valid" label="Enregistrer" severity="success"/>
      </div>
    </form>
  `,
  styleUrls: ["./product-form.component.scss"],
  standalone: true,
  imports: [
    FormsModule,
    Button,
    DropdownModule,
    RatingModule,
    ChipsModule,
    InputTextareaModule,
    PaginatorModule
  ],
  encapsulation: ViewEncapsulation.None
})
export class ProductFormComponent {
  public readonly product = input.required<Product>();
  public readonly isCreation = input.required<boolean>();

  @Output() cancel = new EventEmitter<void>();
  @Output() save = new EventEmitter<{product: Product, file?: File}>();

  private selectedFile?: File;

  public readonly editedProduct = computed(() => ({ ...this.product() }));

  public readonly categories: SelectItem[] = [
    { value: 1, label: "Accessories" },
    { value: 2, label: "Fitness" },
    { value: 3, label: "Clothing" },
    { value: 4, label: "Electronics" },
  ];

  public readonly inventoryStatusOptions: SelectItem[] = [
    { value: "INSTOCK", label: "En stock" },
    { value: "LOWSTOCK", label: "Stock bas" },
    { value: "OUTOFSTOCK", label: "Rupture de stock" },
  ];

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.selectedFile = input.files[0];
    }
  }

  onCancel() {
    this.cancel.emit();
  }

  onSave() {
    this.save.emit({
      product: this.editedProduct(),
      file: this.selectedFile
    });
  }
}
