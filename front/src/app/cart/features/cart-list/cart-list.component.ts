import {Component, inject, OnInit} from '@angular/core';
import {Cart} from "../../data-access/cart.model";
import {CartService} from "../../data-access/cart.service";
import {Button} from "primeng/button";
import {CardModule} from "primeng/card";
import {DataViewModule} from "primeng/dataview";
import {PrimeTemplate} from "primeng/api";
import {Product} from "../../../products/data-access/product.model";
import {MatCardModule} from "@angular/material/card";
import {CurrencyPipe, NgForOf, NgIf} from "@angular/common";
import {MatButtonModule} from "@angular/material/button";

@Component({
  selector: 'app-cart-list',
  standalone: true,
  imports: [
    Button,
    CardModule,
    DataViewModule,
    PrimeTemplate,
    MatCardModule,
    CurrencyPipe,
    NgForOf,
    NgIf,
    MatButtonModule
  ],
  templateUrl: './cart-list.component.html',
  styleUrl: './cart-list.component.css'
})
export class CartListComponent implements OnInit {
  cartItems: Product[] = [];
  total = 0;

  private readonly cartService = inject(CartService);

  ngOnInit(): void {
    this.cartService.getCart().subscribe((items) => {
      this.cartItems = items;
      this.calculeTotalFacture();
    })
  }

  updateCartItemQuantity(product: Product, quantity: number) {
    if (quantity > 0) {
      this.cartService.updateCartItemQuantity(product, quantity);
    } else {
      this.cartService.removeFromCart(product);
    }
  }

  removeFromCart(product: Product){
    this.cartService.removeFromCart(product);
  }

  calculeTotalFacture(){
    this.total = this.cartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  }

}
