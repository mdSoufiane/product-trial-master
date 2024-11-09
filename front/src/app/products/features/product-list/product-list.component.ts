import {Component, OnInit, inject, signal, ViewChild, ChangeDetectorRef} from "@angular/core";
import { Product } from "app/products/data-access/product.model";
import { ProductsService } from "app/products/data-access/products.service";
import { ProductFormComponent } from "app/products/ui/product-form/product-form.component";
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { DataViewModule } from 'primeng/dataview';
import { DialogModule } from 'primeng/dialog';
import {CartService} from "../../../cart/data-access/cart.service";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AsyncPipe, CurrencyPipe, NgClass, NgForOf} from "@angular/common";
import {animate, style, transition, trigger} from "@angular/animations";
import {RouterLink} from "@angular/router";
import {ConfirmationService, MessageService} from "primeng/api";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ToastModule} from "primeng/toast";
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";
import {Observable} from "rxjs";
import {FormsModule} from "@angular/forms";
import {MatInput} from "@angular/material/input";

const emptyProduct: Product = {
  id: 0,
  code: "",
  name: "",
  description: "",
  image: "",
  categoryId: 1,
  price: 0,
  quantity: 0,
  internalReference: "",
  shellId: 0,
  inventoryStatus: "INSTOCK",
  rating: 0,
  createdAt: 0,
  updatedAt: 0,
};

@Component({
  selector: "app-product-list",
  templateUrl: "./product-list.component.html",
  styleUrls: ["./product-list.component.scss"],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('500ms ease-in', style({ opacity: 1 }))
      ])
    ])
  ],
  standalone: true,
  imports: [
    DataViewModule,
    CardModule,
    ButtonModule,
    DialogModule,
    ProductFormComponent,
    MatCardModule,
    MatButtonModule,
    NgForOf,
    RouterLink,
    NgClass,
    CurrencyPipe,
    ConfirmDialogModule,
    ToastModule,
    MatPaginator,
    AsyncPipe,
    FormsModule,
    MatInput
  ],
})
export class ProductListComponent implements OnInit {

  imageUrl: string = ';'
  products: Product[] = [];

  public readonly productsService = inject(ProductsService);
  private readonly cartService = inject(CartService);
  private snackBar = inject(MatSnackBar);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly messageService = inject(MessageService);

  // public readonly products = this.productsService.products;

  public isDialogVisible = false;
  public isCreation = false;
  public readonly editedProduct = signal<Product>(emptyProduct);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  obs!: Observable<any>;
  public dataSource! : MatTableDataSource<Product>;

  quantities: { [productId: number]: number } = {};
  ngOnInit() {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productsService.get().subscribe({
      next: products => {
        this.products = products;
        this.products.forEach((product: Product) => {
          this.productsService.getProductImage(product.id).subscribe(imageUrl => {
            product.image = imageUrl;
            this.quantities[product.id] = 1;
          });
        });
        this.dataSource = new MatTableDataSource<Product>(this.products);
        this.dataSource.paginator = this.paginator;
        this.obs = this.dataSource.connect();
      },
      error: err => {
        console.log(err)
      }
    });
  }


  public onCreate() {
    this.isCreation = true;
    this.isDialogVisible = true;
    this.editedProduct.set(emptyProduct);
  }

  public onUpdate(product: Product) {
    this.isCreation = false;
    this.isDialogVisible = true;
    this.editedProduct.set(product);
  }

  public onDelete(product: Product) {
    this.confirmationService.confirm({
      message: `Êtes-vous sûr de vouloir supprimer le produit "${product.name}" ?`,
      header: 'Confirmation de suppression',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.productsService.delete(product.id).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Produit supprimé avec succès'
            });
            this.loadProducts();
          },
          error: () => {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Erreur lors de la suppression du produit'
            });
          }
        });
      }
    });
  }

  public onSave(event: {product: Product, file?: File}) {
    if (this.isCreation && event.file) {
      this.productsService.create(event.product, event.file).subscribe(() => {
        this.loadProducts(); // Refresh the list after creation
        this.closeDialog();
      });
    } else {
      this.productsService.update(event.product).subscribe(() => {
        this.loadProducts(); // Refresh the list after update
        this.closeDialog();
      });
    }
  }

  public onCancel() {
    this.closeDialog();
  }

  private closeDialog() {
    this.isDialogVisible = false;
    this.isCreation = false;
  }

  addToCart(product: Product) {
    const quantity = this.quantities[product.id] || 1;
    this.cartService.addToCart(product, quantity);
  }

  updateQuantity(productId: number, quantity: number) {
    this.quantities[productId] = quantity;
  }

  removeFromCart(product: Product| undefined) {
    if (product) {
      this.cartService.removeFromCart(product);
      this.snackBar.open(`${product.name} a été retiré du panier.`, 'Fermer', {
        duration: 3000,
        verticalPosition: 'top',
        horizontalPosition: 'center',
        panelClass: ['remove-snackbar']
      });
    }
  }


}
