import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {Product} from "../../products/data-access/product.model";

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private items: Product[] = [];
  private cartItems = new BehaviorSubject<Product[]>([]);

  cartItem$ = this.cartItems.asObservable();

  addToCart(product: Product, quantity: number = 1) {
    const existingProduct = this.items.find(item => item.id === product.id);
    if (existingProduct) {
      existingProduct.quantity += quantity;
    } else {
      this.items.push({ ...product, quantity });
    }
    this.cartItems.next(this.items);
  }

  updateCartItemQuantity(product: Product, quantity: number) {
    const existingProduct = this.items.find(item => item.id === product.id);
    if (existingProduct) {
      existingProduct.quantity = quantity;
    }
    this.cartItems.next(this.items);
  }

  getCart(){
    return this.cartItem$;
  }

  removeFromCart(product: Product) {
    const existingProduct = this.items.find(item => item.id === product.id);
    if (existingProduct) {
        this.items = this.items.filter(item => item.id !== product.id);
    }
    this.cartItems.next(this.items);
  }

  getCartItemCount(){
    return this.items.length;
  }

}
