import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, RouterLink} from "@angular/router";
import {ProductsService} from "../../data-access/products.service";
import {CartService} from "../../../cart/data-access/cart.service";
import {Product} from "../../data-access/product.model";
import {CurrencyPipe, NgClass, NgIf} from "@angular/common";
import {MatCardModule} from "@angular/material/card";
import {MatAnchor} from "@angular/material/button";

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [
    NgIf,
    MatCardModule,
    CurrencyPipe,
    NgClass,
    MatAnchor,
    RouterLink
  ],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.css'
})
export class ProductDetailComponent implements OnInit {

  product! : Product;

  private route = inject(ActivatedRoute);
  private productService = inject(ProductsService);
  private cartService = inject(CartService);

  ngOnInit(): void {
    this.getProductById();
  }

  getProductById() : void{
    const productId = this.route.snapshot.params['id'];
    if(productId != null){
      this.productService.getProductById(productId).subscribe({
        next: data => {
          this.product = data;
          this.productService.getProductImage(this.product.id).subscribe(imageUrl => {
            this.product.image = imageUrl;
          });
        }, error: error => {
          console.log("Error while getting the products : ",error);
        }
      });
    }
  }

  addToCart(product: Product){
    if(product.quantity > 0){
      this.cartService.addToCart(product);
    }
  }

}
