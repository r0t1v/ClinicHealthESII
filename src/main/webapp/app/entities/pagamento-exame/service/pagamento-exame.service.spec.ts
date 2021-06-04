import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPagamentoExame, PagamentoExame } from '../pagamento-exame.model';

import { PagamentoExameService } from './pagamento-exame.service';

describe('Service Tests', () => {
  describe('PagamentoExame Service', () => {
    let service: PagamentoExameService;
    let httpMock: HttpTestingController;
    let elemDefault: IPagamentoExame;
    let expectedResult: IPagamentoExame | IPagamentoExame[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PagamentoExameService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        formapagamento: 'AAAAAAA',
        conteudo: 'AAAAAAA',
        seliquidado: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PagamentoExame', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PagamentoExame()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PagamentoExame', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            formapagamento: 'BBBBBB',
            conteudo: 'BBBBBB',
            seliquidado: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PagamentoExame', () => {
        const patchObject = Object.assign(
          {
            seliquidado: true,
          },
          new PagamentoExame()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PagamentoExame', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            formapagamento: 'BBBBBB',
            conteudo: 'BBBBBB',
            seliquidado: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PagamentoExame', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPagamentoExameToCollectionIfMissing', () => {
        it('should add a PagamentoExame to an empty array', () => {
          const pagamentoExame: IPagamentoExame = { id: 123 };
          expectedResult = service.addPagamentoExameToCollectionIfMissing([], pagamentoExame);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pagamentoExame);
        });

        it('should not add a PagamentoExame to an array that contains it', () => {
          const pagamentoExame: IPagamentoExame = { id: 123 };
          const pagamentoExameCollection: IPagamentoExame[] = [
            {
              ...pagamentoExame,
            },
            { id: 456 },
          ];
          expectedResult = service.addPagamentoExameToCollectionIfMissing(pagamentoExameCollection, pagamentoExame);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PagamentoExame to an array that doesn't contain it", () => {
          const pagamentoExame: IPagamentoExame = { id: 123 };
          const pagamentoExameCollection: IPagamentoExame[] = [{ id: 456 }];
          expectedResult = service.addPagamentoExameToCollectionIfMissing(pagamentoExameCollection, pagamentoExame);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pagamentoExame);
        });

        it('should add only unique PagamentoExame to an array', () => {
          const pagamentoExameArray: IPagamentoExame[] = [{ id: 123 }, { id: 456 }, { id: 27113 }];
          const pagamentoExameCollection: IPagamentoExame[] = [{ id: 123 }];
          expectedResult = service.addPagamentoExameToCollectionIfMissing(pagamentoExameCollection, ...pagamentoExameArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const pagamentoExame: IPagamentoExame = { id: 123 };
          const pagamentoExame2: IPagamentoExame = { id: 456 };
          expectedResult = service.addPagamentoExameToCollectionIfMissing([], pagamentoExame, pagamentoExame2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pagamentoExame);
          expect(expectedResult).toContain(pagamentoExame2);
        });

        it('should accept null and undefined values', () => {
          const pagamentoExame: IPagamentoExame = { id: 123 };
          expectedResult = service.addPagamentoExameToCollectionIfMissing([], null, pagamentoExame, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pagamentoExame);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
