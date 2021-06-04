import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PagamentoExameDetailComponent } from './pagamento-exame-detail.component';

describe('Component Tests', () => {
  describe('PagamentoExame Management Detail Component', () => {
    let comp: PagamentoExameDetailComponent;
    let fixture: ComponentFixture<PagamentoExameDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PagamentoExameDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ pagamentoExame: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PagamentoExameDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PagamentoExameDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pagamentoExame on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pagamentoExame).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
