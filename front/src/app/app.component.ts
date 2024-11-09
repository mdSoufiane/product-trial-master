import {
  Component, inject, OnInit,
} from "@angular/core";
import { RouterModule } from "@angular/router";
import { SplitterModule } from 'primeng/splitter';
import { ToolbarModule } from 'primeng/toolbar';
import { PanelMenuComponent } from "./shared/ui/panel-menu/panel-menu.component";
import {CartService} from "./cart/data-access/cart.service";
import {NgIf} from "@angular/common";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
  standalone: true,
  imports: [RouterModule, SplitterModule, ToolbarModule, PanelMenuComponent, NgIf],
})
export class AppComponent implements OnInit{
  title = "ALTEN SHOP";
  cartItemCount: number = 0;

  private carteService = inject(CartService);

  ngOnInit(): void {
    this.carteService.getCart().subscribe(() => {
      this.cartItemCount = this.carteService.getCartItemCount();
    })
  }
}
