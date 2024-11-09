export interface Product {
    id: number;
    code: string;
    name: string;
    description: string;
    image: string;
    categoryId: number;
    price: number;
    quantity: number;
    internalReference: string;
    shellId: number;
    inventoryStatus: "INSTOCK" | "LOWSTOCK" | "OUTOFSTOCK";
    rating: number;
    createdAt: number;
    updatedAt: number;
}
