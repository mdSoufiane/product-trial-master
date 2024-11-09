import {Product} from "../../products/data-access/product.model";

export interface Cart{
  product: Product;
  quantity: number;
}
