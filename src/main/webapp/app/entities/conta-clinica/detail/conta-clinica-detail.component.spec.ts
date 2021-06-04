import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContaClinicaDetailComponent } from './conta-clinica-detail.component';

describe('Component Tests', () => {
  describe('ContaClinica Management Detail Component', () => {
    let comp: ContaClinicaDetailComponent;
    let fixture: ComponentFixture<ContaClinicaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContaClinicaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contaClinica: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContaClinicaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContaClinicaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contaClinica on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contaClinica).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
