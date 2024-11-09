import { Injectable, inject, signal } from "@angular/core";
import { Product } from "./product.model";
import { HttpClient } from "@angular/common/http";
import {catchError, map, Observable, of, tap} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable({
    providedIn: "root"
}) export class ProductsService {

    private readonly http = inject(HttpClient);
    private readonly path = "/api/products";

    private readonly _products = signal<Product[]>([]);

    public readonly products = this._products.asReadonly();

    public get(): Observable<Product[]> {
        return this.http.get<Product[]>(`${environment.backendHost}${this.path}`).pipe(
            catchError((error) => {
                return this.http.get<Product[]>("assets/products.json");
            }),
            tap((products) => this._products.set(products)),
        );
    }

  public getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${environment.backendHost}${this.path}/${id}`);
  }

  public create(product: Product, file: File): Observable<boolean> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('code', product.code);
    formData.append('name', product.name);
    formData.append('description', product.description);
    formData.append('price', product.price.toString());
    formData.append('quantity', product.quantity.toString());
    formData.append('internalReference', product.internalReference);
    formData.append('inventoryStatus', product.inventoryStatus);
    formData.append('rating', product.rating.toString());
    formData.append('categoryId', product.categoryId.toString());

    return this.http.post<boolean>(`${environment.backendHost}${this.path}`, formData).pipe(
      catchError(() => of(true)),
      tap(() => this._products.update(products => [product, ...products])),
    );
  }

  public update(product: Product): Observable<boolean> {
    const productDto = {
      code: product.code,
      name: product.name,
      description: product.description,
      price: product.price,
      quantity: product.quantity,
      internalReference: product.internalReference,
      inventoryStatus: product.inventoryStatus,
      rating: product.rating,
      categoryId: product.categoryId
    };

    return this.http.patch<boolean>(
      `${environment.backendHost}${this.path}/${product.id}`,
      productDto
    ).pipe(
      catchError(() => of(true)),
      tap(() => this._products.update(products =>
        products.map(p => p.id === product.id ? product : p)
      )),
    );
  }

  public delete(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.backendHost}${this.path}/${id}`).pipe(
      tap(() => {
        this._products.update(products => products.filter(p => p.id !== id));
      }),
      catchError(() => {
        console.error('Error deleting product');
        return of(void 0);
      })
    );
  }

    public getProductImage(productId: number){
      const url = `${environment.backendHost}${this.path}/productImage/${productId}`;
      return this.http.get(url, { responseType: 'blob' }).pipe(
        map(blob => URL.createObjectURL(blob))
      );
    }
}
