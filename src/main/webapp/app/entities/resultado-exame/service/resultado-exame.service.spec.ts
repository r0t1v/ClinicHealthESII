import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IResultadoExame, ResultadoExame } from '../resultado-exame.model';

import { ResultadoExameService } from './resultado-exame.service';

describe('Service Tests', () => {
  describe('ResultadoExame Service', () => {
    let service: ResultadoExameService;
    let httpMock: HttpTestingController;
    let elemDefault: IResultadoExame;
    let expectedResult: IResultadoExame | IResultadoExame[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ResultadoExameService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        descricao: 'AAAAAAA',
        prescricao: 'AAAAAAA',
        indicacao: 'AAAAAAA',
        contraindicacao: 'AAAAAAA',
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

      it('should create a ResultadoExame', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ResultadoExame()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ResultadoExame', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            descricao: 'BBBBBB',
            prescricao: 'BBBBBB',
            indicacao: 'BBBBBB',
            contraindicacao: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ResultadoExame', () => {
        const patchObject = Object.assign(
          {
            descricao: 'BBBBBB',
            indicacao: 'BBBBBB',
          },
          new ResultadoExame()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ResultadoExame', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            descricao: 'BBBBBB',
            prescricao: 'BBBBBB',
            indicacao: 'BBBBBB',
            contraindicacao: 'BBBBBB',
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

      it('should delete a ResultadoExame', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addResultadoExameToCollectionIfMissing', () => {
        it('should add a ResultadoExame to an empty array', () => {
          const resultadoExame: IResultadoExame = { id: 123 };
          expectedResult = service.addResultadoExameToCollectionIfMissing([], resultadoExame);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(resultadoExame);
        });

        it('should not add a ResultadoExame to an array that contains it', () => {
          const resultadoExame: IResultadoExame = { id: 123 };
          const resultadoExameCollection: IResultadoExame[] = [
            {
              ...resultadoExame,
            },
            { id: 456 },
          ];
          expectedResult = service.addResultadoExameToCollectionIfMissing(resultadoExameCollection, resultadoExame);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ResultadoExame to an array that doesn't contain it", () => {
          const resultadoExame: IResultadoExame = { id: 123 };
          const resultadoExameCollection: IResultadoExame[] = [{ id: 456 }];
          expectedResult = service.addResultadoExameToCollectionIfMissing(resultadoExameCollection, resultadoExame);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(resultadoExame);
        });

        it('should add only unique ResultadoExame to an array', () => {
          const resultadoExameArray: IResultadoExame[] = [{ id: 123 }, { id: 456 }, { id: 3599 }];
          const resultadoExameCollection: IResultadoExame[] = [{ id: 123 }];
          expectedResult = service.addResultadoExameToCollectionIfMissing(resultadoExameCollection, ...resultadoExameArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const resultadoExame: IResultadoExame = { id: 123 };
          const resultadoExame2: IResultadoExame = { id: 456 };
          expectedResult = service.addResultadoExameToCollectionIfMissing([], resultadoExame, resultadoExame2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(resultadoExame);
          expect(expectedResult).toContain(resultadoExame2);
        });

        it('should accept null and undefined values', () => {
          const resultadoExame: IResultadoExame = { id: 123 };
          expectedResult = service.addResultadoExameToCollectionIfMissing([], null, resultadoExame, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(resultadoExame);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
