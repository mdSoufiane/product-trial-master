import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from "@angular/common";


@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.css'
})
export class ContactComponent implements OnInit{
  contactForm!: FormGroup;
  submitted = false;

  private formBuilder = inject(FormBuilder);

  ngOnInit(): void {
    this.contactForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      message: ['', [Validators.required, Validators.maxLength(300)]]
    });
  }

  get f() {
    return this.contactForm.controls;
  }

  onSubmit(): void{
    this.submitted = true;
    if (this.contactForm.invalid){
      return;
    }

    console.log("Form submitted successfully : ", this.contactForm.value);


    this.contactForm.reset();
  }


}
