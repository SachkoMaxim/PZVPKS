
with Ada.Text_IO, Ada.Integer_Text_IO, Ada.Real_Time;
use Ada.Text_IO, Ada.Integer_Text_IO, Ada.Real_Time;

procedure Lab5 is

   N: Integer := 2000;  -- Розмір векторів і квадратних матриць
   P: Integer := 8;   -- Кількість процесорів
   H: Integer := N/P; -- Розмір підвектора і кількість стовпців підматриці

   Start_Time, End_Time: Time;
   Elapsed_Time: Time_Span;

   -- Формування типів
   type Vector_General is array(Integer range <>) of Integer;
   subtype Vector_N is Vector_General(1..N);
   subtype Vector_H is Vector_General (1..H);
   subtype Vector_2H is Vector_General (1..2*H);
   subtype Vector_3H is Vector_General (1..3*H);
   subtype Vector_4H is Vector_General (1..4*H);

   type Matrix_General is array(Integer range <>) of Vector_N;
   type Matrix_N is array(1..N) of Vector_N;
   type Matrix_H is array(1..N) of Vector_H;
   type Matrix_2H is array(1..N) of Vector_2H;
   type Matrix_3H is array(1..N) of Vector_3H;
   type Matrix_4H is array(1..N) of Vector_4H;

   -- Допоміжні функції
   function Fill_Vector return Vector_N is
      Vector: Vector_N;
   begin
      for I in 1..N loop
         Vector(I) := 1;
      end loop;
      return Vector;
   end Fill_Vector;

   function Fill_Matrix return Matrix_N is
      Matrix: Matrix_N;
   begin
      for I in 1..N loop
         for J in 1..N loop
            Matrix(I)(J) := 1;
         end loop;
      end loop;
      return Matrix;
   end Fill_Matrix;

   function Scalar_Product (Vector1, Vector2: Vector_H) return Integer is
      Result: Integer := 0;
   begin
      for I in 1..H loop
         Result := Result + (Vector1(I) * Vector2(I));
      end loop;
      return Result;
   end Scalar_Product;

   function Multiply_Vector_Matrix (Vector: Vector_N; Matrix: Matrix_H) return Vector_H is
      Result: Vector_H := (others => 0);
   begin
      for I in 1..H loop
         for J in 1..N loop
            Result(I) := Result(I) + (Vector(J) * Matrix(J)(I));
         end loop;
      end loop;
      return Result;
   end Multiply_Vector_Matrix;

   function Multiply_Matrices (Matrix1: Matrix_N; Matrix2: Matrix_H) return Matrix_H is
      Result: Matrix_H := (others => (others => 0));
   begin
      for I in 1..N loop
         for J in 1..H loop
            for K in 1..N loop
               Result(I)(J) := Result(I)(J) + (Matrix1(I)(K) * Matrix2(K)(J));
            end loop;
         end loop;
      end loop;
      return Result;
   end Multiply_Matrices;

   function Calculate_Ah (Scalar: Integer; Vector1, Vector2: Vector_H) return Vector_H is
      Result: Vector_H := (others => 0);
   begin
      for I in 1..H loop
         Result(I) := (Scalar * Vector1(I)) + Vector2(I);
      end loop;
      return Result;
   end Calculate_Ah;

   -- Допоміжні процедури
   procedure Print_Vector (Vector: Vector_N; Name : String) is
   begin
      Put ("Calculation result: " & Name & " = [ ");
      for I in Vector'Range loop
         Put (Integer'Image (Vector(I)) & ", ");
      end loop;
      Put ("]");
      New_Line;
   end Print_Vector;

---------------------------------------------------------------------

   -- Опис специфікації задач
   task T1 is
      -- Задавання розміру сховища у 2GB
      pragma Storage_Size(2_147_483_648);
      entry MXMM4h_fromT2_i_B4h_toT2 (
         MX_IN : in Matrix_N;
         MM4h_IN : in Matrix_4H;

         B4h_OUT : out Vector_4H
      );
      entry a_fromT2 (a_IN : in Integer);
      entry A3h_fromT3 (A3h_IN : in Vector_3H);
      entry A4h_fromT2 (A4h_IN : in Vector_4H);
   end T1;

   task T2 is
      -- Задавання розміру сховища у 2GB
      pragma Storage_Size(2_147_483_648);
      entry ZDChMRh_fromT4_i_B3hMXMM3h_toT4 (
         Z_IN : in Vector_N;
         D_IN : in Vector_N;
         Ch_IN : in Vector_H;
         MRh_IN : in Matrix_H;

         B3h_OUT : out Vector_3H;
         MX_OUT : out Matrix_N;
         MM3h_OUT : out Matrix_3H
      );
      entry a1_fromT1 (a1_IN : in Integer);
      entry a_fromT4 (a_IN : in Integer);
      entry A3h_fromT4 (A3h_IN : in Vector_3H);
   end T2;

   task T3 is
      -- Задавання розміру сховища у 2GB
      pragma Storage_Size(2_147_483_648);
      entry B3hMXMM3h_fromT1_i_ZDChMRh_toT1 (
         B3h_IN : in Vector_3H;
         MX_IN : in Matrix_N;
         MM3h_IN : in Matrix_3H;

         Z_OUT : out Vector_N;
         D_OUT : out Vector_N;
         Ch_OUT : out Vector_H;
         MRh_OUT : out Matrix_H
      );
      entry a_fromT5 (a_IN : in Integer);
      entry A2h_fromT5 (A2h_IN : in Vector_2H);
   end T3;

   task T4 is
      -- Задавання розміру сховища у 2GB
      pragma Storage_Size(2_147_483_648);
      entry ZD_fromT3_i_C4hMR4h_toT3 (
         Z_IN : in Vector_N;
         D_IN : in Vector_N;

         C4h_OUT : out Vector_4H;
         MR4h_OUT : out Matrix_4H
      );
      entry a3_fromT3 (a3_IN : in Integer);
      entry a2_fromT2 (a2_IN : in Integer);
      entry A2h_fromT6 (A2h_IN : in Vector_2H);
   end T4;

   task T5 is
      -- Задавання розміру сховища у 2GB
      pragma Storage_Size(2_147_483_648);
      entry B2hMXMM2hZDC2hMR2h_fromT3 (
         B2h_IN : in Vector_2H;
         MX_IN : in Matrix_N;
         MM2h_IN : in Matrix_2H;
         Z_IN : in Vector_N;
         D_IN : in Vector_N;
         C2h_IN : in Vector_2H;
         MR2h_IN : in Matrix_2H
      );
      entry Ah_fromT7 (Ah_IN : in Vector_H);
   end T5;

   task T6 is
      -- Задавання розміру сховища у 2GB
      pragma Storage_Size(2_147_483_648);
      entry B2hMXMM2hZDC2hMR2h_fromT4 (
         B2h_IN : in Vector_2H;
         MX_IN : in Matrix_N;
         MM2h_IN : in Matrix_2H;
         Z_IN : in Vector_N;
         D_IN : in Vector_N;
         C2h_IN : in Vector_2H;
         MR2h_IN : in Matrix_2H
      );
      entry a5_fromT5 (a5_IN : in Integer);
      entry a4_fromT4 (a4_IN : in Integer);
      entry a8_fromT8 (a8_IN : in Integer);
      entry a_fromT6 (a_OUT : out Integer);
      entry Ah_fromT8 (Ah_IN : in Vector_H);
   end T6;

   task T7 is
      -- Задавання розміру сховища у 2GB
      pragma Storage_Size(2_147_483_648);
      entry BhMXMMhZDChMRh_fromT5 (
         Bh_IN : in Vector_H;
         MX_IN : in Matrix_N;
         MMh_IN : in Matrix_H;
         Z_IN : in Vector_N;
         D_IN : in Vector_N;
         Ch_IN : in Vector_H;
         MRh_IN : in Matrix_H
      );
      entry a_fromT8 (a_IN : in Integer);
   end T7;

   task T8 is
      -- Задавання розміру сховища у 2GB
      pragma Storage_Size(2_147_483_648);
      entry BhMXMMhZDChMRh_fromT6 (
         Bh_IN : in Vector_H;
         MX_IN : in Matrix_N;
         MMh_IN : in Matrix_H;
         Z_IN : in Vector_N;
         D_IN : in Vector_N;
         Ch_IN : in Vector_H;
         MRh_IN : in Matrix_H
      );
      entry a7_fromT7 (a7_IN : in Integer);
   end T8;

---------------------------------------------------------------------

   -- Опис тіл задач

   -- Задачі будуть обробляти такі частини векторів і матриць
   -- T1 - 1 частина
   -- T3 - 2 частина
   -- T5 - 3 частина
   -- T7 - 4 частина
   -- T2 - 5 частина
   -- T4 - 6 частина
   -- T6 - 7 частина
   -- T8 - 8 частина

   task body T1 is
      a1, a: Integer;
      B, Z, D, A_res: Vector_N;
      Ch, ZMMh, DMXMRh: Vector_H;
      MX: Matrix_N;
      MM4h: Matrix_4H;
      MRh, MXMRh: Matrix_H;
      Get_A3h_fromT3, Get_A4h_fromT2: Boolean := False;
   begin
      Put_Line("Task T1 is started");

      -- Введення B
      B := Fill_Vector;

      -- Передати в задачу T2 дані: B4н
      -- Прийняти дані від задачі T2: MX, MM4н
      accept MXMM4h_fromT2_i_B4h_toT2 (
         MX_IN : in Matrix_N;
         MM4h_IN : in Matrix_4H;

         B4h_OUT : out Vector_4H
      ) do
         MX := MX_IN;
         MM4h := MM4h_IN;

         B4h_OUT := B(1+4*H..8*H);
      end MXMM4h_fromT2_i_B4h_toT2;

      -- Передати в задачу T3 дані: B3н, MX, MM3н
      -- Прийняти дані від задачі T3: Z, D, Cн, MRн
      T3.B3hMXMM3h_fromT1_i_ZDChMRh_toT1 (
         B(1+H..4*H),
         MX,
         (for I in 1..N => MM4h(I)(H+1..4*H)),
         Z,
         D,
         Ch,
         MRh
      );

      -- Обчислення1 а1 = (Bн * Cн)
      a1 := Scalar_Product(B(1..H), Ch);

      -- Передати в задачу T2 дані: a1
      T2.a1_fromT1(a1);

      -- Прийняти дані від задачі T2: a
      accept a_fromT2 (a_IN : in Integer) do
         a := a_IN;
      end a_fromT2;

      -- Обчислення3 Aн = a * (Z * MMн) + D * (MX * MRн)
      -- (Z * MMн)
      ZMMh := Multiply_Vector_Matrix(Z, (for I in 1..N => MM4h(I)(1..H)));

      -- (MX * MRн)
      MXMRh := Multiply_Matrices(MX, MRh);

      -- D * (MX * MRн)
      DMXMRh := Multiply_Vector_Matrix(D, MXMRh);

      -- Aн = a * (Z * MMн) + D * (MX * MRн)
      A_res(1..H) := Calculate_Ah(a, ZMMh, DMXMRh);

      -- Прийняти дані від задачі T3: A3н
      -- Прийняти дані від задачі T2: A4н
      while not (Get_A3h_fromT3 and Get_A4h_fromT2) loop
         select
            -- Прийняти дані від задачі T3: A3н
            accept A3h_fromT3 (A3h_IN : in Vector_3H) do
               A_res(1+H..4*H) := A3h_IN;
            end A3h_fromT3;

            Get_A3h_fromT3 := True;
         or
            -- Прийняти дані від задачі T2: A4н
            accept A4h_fromT2 (A4h_IN : in Vector_4H) do
               A_res(1+4*H..8*H) := A4h_IN;
            end A4h_fromT2;

            Get_A4h_fromT2 := True;
         end select;
      end loop;

      -- Виведення результату A
      Print_Vector(A_res, "A");

      Put_Line("Task T1 is finished");

      -- Вимірювання часу виконання
      End_Time := Clock;
      Elapsed_Time := End_Time - Start_Time;

      New_Line;

      Put_Line("Lab5: execution time is " & Duration'Image(To_Duration(Elapsed_Time)) & " seconds");
   end T1;

---------------------------------------------------------------------

   task body T2 is
      a2, a1, a: Integer;
      Z, D: Vector_N;
      B4h, A4h: Vector_4H;
      Ch, ZMMh, DMXMRh: Vector_H;
      MX, MM: Matrix_N;
      MRh, MXMRh: Matrix_H;
   begin
      Put_Line("Task T2 is started");

      -- Введення MX, MM
      MX := Fill_Matrix;
      MM := Fill_Matrix;

      -- Передати в задачу T1 дані: MX, MM4н
      -- Прийняти дані від задачі T1: B4н
      T1.MXMM4h_fromT2_i_B4h_toT2 (
         MX,
         (for I in 1..N => MM(I)(1..4*H)),
         B4h
      );

      -- Передати в задачу T4 дані: B3н, MX, MM3н
      -- Прийняти дані від задачі T4: Z, D, Cн, MRн
      accept ZDChMRh_fromT4_i_B3hMXMM3h_toT4 (
         Z_IN : in Vector_N;
         D_IN : in Vector_N;
         Ch_IN : in Vector_H;
         MRh_IN : in Matrix_H;

         B3h_OUT : out Vector_3H;
         MX_OUT : out Matrix_N;
         MM3h_OUT : out Matrix_3H
      ) do
         Z := Z_IN;
         D := D_IN;
         Ch := Ch_IN;
         MRh := MRh_IN;

         B3h_OUT := B4h(1+H..4*H);
         MX_OUT := MX;
         MM3h_OUT := (for I in 1..N => MM(I)(1+5*H..8*H));
      end ZDChMRh_fromT4_i_B3hMXMM3h_toT4;

      -- Обчислення1 а2 = (Bн * Cн)
      a2 := Scalar_Product(B4h(1..H), Ch);

      -- Прийняти дані від задачі T1: a1
      accept a1_fromT1 (a1_IN : in Integer) do
         a1 := a1_IN;
      end a1_fromT1;

      -- Обчислення2 a2 = a2 + a1
      a2 := a2 + a1;

      -- Передати в задачу T4 дані: a2
      T4.a2_fromT2(a2);

      -- Прийняти дані від задачі T4: a
      accept a_fromT4 (a_IN : in Integer) do
         a := a_IN;
      end a_fromT4;

      -- Передати в задачу T1 дані: a
      T1.a_fromT2(a);

      -- Обчислення3 Aн = a * (Z * MMн) + D * (MX * MRн)
      -- (Z * MMн)
      ZMMh := Multiply_Vector_Matrix(Z, (for I in 1..N => MM(I)(1+4*H..5*H)));

      -- (MX * MRн)
      MXMRh := Multiply_Matrices(MX, MRh);

      -- D * (MX * MRн)
      DMXMRh := Multiply_Vector_Matrix(D, MXMRh);

      -- Aн = a * (Z * MMн) + D * (MX * MRн)
      A4h(1..H) := Calculate_Ah(a, ZMMh, DMXMRh);

      -- Прийняти дані від задачі T4: A3н
      accept A3h_fromT4 (A3h_IN : in Vector_3H) do
         A4h(1+H..4*H) := A3h_IN;
      end A3h_fromT4;

      -- Передати в задачу T1 дані: A4н
      T1.A4h_fromT2(A4h);

      Put_Line("Task T2 is finished");
   end T2;

---------------------------------------------------------------------

   task body T3 is
      a3, a: Integer;
      Z, D: Vector_N;
      C4h: Vector_4H;
      B3h, A3h: Vector_3H;
      ZMMh, DMXMRh: Vector_H;
      MX: Matrix_N;
      MR4h: Matrix_4H;
      MM3h: Matrix_3H;
      MXMRh: Matrix_H;
   begin
      Put_Line("Task T3 is started");

      -- Введення Z, D
      Z := Fill_Vector;
      D := Fill_Vector;

      -- Передати в задачу T4 дані: Z, D
      -- Прийняти дані від задачі T4: C4н, MR4н
      T4.ZD_fromT3_i_C4hMR4h_toT3 (
         Z, D, C4h, MR4h
      );

      -- Передати в задачу T1 дані: Z, D, Cн, MRн
      -- Прийняти дані від задачі T1: B3н, MX, MM3н
      accept B3hMXMM3h_fromT1_i_ZDChMRh_toT1 (
         B3h_IN : in Vector_3H;
         MX_IN : in Matrix_N;
         MM3h_IN : in Matrix_3H;

         Z_OUT : out Vector_N;
         D_OUT : out Vector_N;
         Ch_OUT : out Vector_H;
         MRh_OUT : out Matrix_H
      ) do
         B3h := B3h_IN;
         MX := MX_IN;
         MM3h := MM3h_IN;

         Z_OUT := Z;
         D_OUT := D;
         Ch_OUT := C4h(1..H);
         MRh_OUT := (for I in 1..N => MR4h(I)(1..H));
      end B3hMXMM3h_fromT1_i_ZDChMRh_toT1;

      -- Передати в задачу T5 дані: B2н, MX, MM2н, Z, D, C2н, MR2н
      T5.B2hMXMM2hZDC2hMR2h_fromT3 (
         B3h(1+H..3*H),
         MX,
         (for I in 1..N => MM3h(I)(1+H..3*H)),
         Z,
         D,
         C4h(1+2*H..4*H),
         (for I in 1..N => MR4h(I)(1+2*H..4*H))
      );

      -- Обчислення1 а3 = (Bн * Cн)
      a3 := Scalar_Product(B3h(1..H), C4h(1+H..2*H));

      -- Передати в задачу T4 дані: a3
      T4.a3_fromT3(a3);

      -- Прийняти дані від задачі T5: a
      accept a_fromT5 (a_IN : in Integer) do
         a := a_IN;
      end a_fromT5;

      -- Обчислення3 Aн = a * (Z * MMн) + D * (MX * MRн)
      -- (Z * MMн)
      ZMMh := Multiply_Vector_Matrix(Z, (for I in 1..N => MM3h(I)(1..H)));

      -- (MX * MRн)
      MXMRh := Multiply_Matrices(MX, (for I in 1..N => MR4h(I)(1+H..2*H)));

      -- D * (MX * MRн)
      DMXMRh := Multiply_Vector_Matrix(D, MXMRh);

      -- Aн = a * (Z * MMн) + D * (MX * MRн)
      A3h(1..H) := Calculate_Ah(a, ZMMh, DMXMRh);

      -- Прийняти дані від задачі T5: A2н
      accept A2h_fromT5 (A2h_IN : in Vector_2H) do
         A3h(1+H..3*H) := A2h_IN;
      end A2h_fromT5;

      -- Передати в задачу T1 дані: A3н
      T1.A3h_fromT3(A3h);

      Put_Line("Task T3 is finished");
   end T3;

---------------------------------------------------------------------

   task body T4 is
      a4, a3, a2, a: Integer;
      C, Z, D: Vector_N;
      B3h, A3h: Vector_3H;
      ZMMh, DMXMRh: Vector_H;
      MR, MX: Matrix_N;
      MM3h: Matrix_3H;
      MXMRh: Matrix_H;
      Get_a_fromT3, Get_a_fromT2: Boolean := False;
   begin
      Put_Line("Task T4 is started");

      -- Введення C, MR
      C := Fill_Vector;
      MR := Fill_Matrix;

      -- Передати в задачу T3 дані: C4н, MR4н
      -- Прийняти дані від задачі T3: Z, D
      accept ZD_fromT3_i_C4hMR4h_toT3 (
         Z_IN : in Vector_N;
         D_IN : in Vector_N;

         C4h_OUT : out Vector_4H;
         MR4h_OUT : out Matrix_4H
      ) do
         Z := Z_IN;
         D := D_IN;

         C4h_OUT := C(1..4*H);
         MR4h_OUT := (for I in 1..N => MR(I)(1..4*H));
      end ZD_fromT3_i_C4hMR4h_toT3;

      -- Передати в задачу T2 дані: Z, D, Cн, MRн
      -- Прийняти дані від задачі T2: B3н, MX, MM3н
      T2.ZDChMRh_fromT4_i_B3hMXMM3h_toT4 (
         Z,
         D,
         C(1+4*H..5*H),
         (for I in 1..N => MR(I)(1+4*H..5*H)),
         B3h,
         MX,
         MM3h
      );

      -- Передати в задачу T6 дані: B2н, MX, MM2н, Z, D, C2н, MR2н
      T6.B2hMXMM2hZDC2hMR2h_fromT4 (
         B3h(1+H..3*H),
         MX,
         (for I in 1..N => MM3h(I)(1+H..3*H)),
         Z,
         D,
         C(1+6*H..8*H),
         (for I in 1..N => MR(I)(1+6*H..8*H))
      );

      -- Обчислення1 а4 = (Bн * Cн)
      a4 := Scalar_Product(B3h(1..H), C(1+5*H..6*H));

      -- Прийняти дані від задачі T3: a3
      -- Обчислення2 a4 = a4 + a3
      -- Прийняти дані від задачі T2: a2
      -- Обчислення2 a4 = a4 + a2
      while not (Get_a_fromT3 and Get_a_fromT2) loop
         select
            -- Прийняти дані від задачі T3: a3
            accept a3_fromT3 (a3_IN : in Integer) do
               a3 := a3_IN;
            end a3_fromT3;

            -- Обчислення2 a4 = a4 + a3
            a4 := a4 + a3;

            Get_a_fromT3 := True;
         or
            -- Прийняти дані від задачі T2: a2
            accept a2_fromT2 (a2_IN : in Integer) do
               a2 := a2_IN;
            end a2_fromT2;

            -- Обчислення2 a4 = a4 + a2
            a4 := a4 + a2;

            Get_a_fromT2 := True;
         end select;
      end loop;

      -- Передати в задачу T6 дані: a4
      T6.a4_fromT4(a4);

      -- Прийняти дані від задачі T6: a
      T6.a_fromT6(a);

      -- Передати в задачу T2 дані: a
      T2.a_fromT4(a);

      -- Обчислення3 Aн = a * (Z * MMн) + D * (MX * MRн)
      -- (Z * MMн)
      ZMMh := Multiply_Vector_Matrix(Z, (for I in 1..N => MM3h(I)(1..H)));

      -- (MX * MRн)
      MXMRh := Multiply_Matrices(MX, (for I in 1..N => MR(I)(1+5*H..6*H)));

      -- D * (MX * MRн)
      DMXMRh := Multiply_Vector_Matrix(D, MXMRh);

      -- Aн = a * (Z * MMн) + D * (MX * MRн)
      A3h(1..H) := Calculate_Ah(a, ZMMh, DMXMRh);

      -- Прийняти дані від задачі T6: A2н
      accept A2h_fromT6 (A2h_IN : in Vector_2H) do
         A3h(1+H..3*H) := A2h_IN;
      end A2h_fromT6;

      -- Передати в задачу T2 дані: A3н
      T2.A3h_fromT4(A3h);

      Put_Line("Task T4 is finished");
   end T4;

---------------------------------------------------------------------

   task body T5 is
      a5, a: Integer;
      Z, D: Vector_N;
      B2h, C2h, A2h: Vector_2H;
      ZMMh, DMXMRh: Vector_H;
      MX: Matrix_N;
      MM2h, MR2h: Matrix_2H;
      MXMRh: Matrix_H;
   begin
      Put_Line("Task T5 is started");

      -- Прийняти дані від задачі T3: B2н, MX, MM2н, Z, D, C2н, MR2н
      accept B2hMXMM2hZDC2hMR2h_fromT3 (
         B2h_IN : in Vector_2H;
         MX_IN : in Matrix_N;
         MM2h_IN : in Matrix_2H;
         Z_IN : in Vector_N;
         D_IN : in Vector_N;
         C2h_IN : in Vector_2H;
         MR2h_IN : in Matrix_2H
      ) do
         B2h := B2h_IN;
         MX := MX_IN;
         MM2h := MM2h_IN;
         Z := Z_IN;
         D := D_IN;
         C2h := C2h_In;
         MR2h := MR2h_IN;
      end B2hMXMM2hZDC2hMR2h_fromT3;

      -- Передати в задачу T7 дані: Bн, MX, MMн, Z, D, Cн, MRн
      T7.BhMXMMhZDChMRh_fromT5(
         B2h(1+H..2*H),
         MX,
         (for I in 1..N => MM2h(I)(1+H..2*H)),
         Z,
         D,
         C2h(1+H..2*H),
         (for I in 1..N => MR2h(I)(1+H..2*H))
      );

      -- Обчислення1 а5 = (Bн * Cн)
      a5 := Scalar_Product(B2h(1..H), C2h(1..H));

      -- Передати в задачу T6 дані: a5
      T6.a5_fromT5(a5);

      -- Прийняти дані від задачі T6: a
      T6.a_fromT6(a);

      -- Передати в задачу T3 дані: a
      T3.a_fromT5(a);

      -- Обчислення3 Aн = a * (Z * MMн) + D * (MX * MRн)
      -- (Z * MMн)
      ZMMh := Multiply_Vector_Matrix(Z, (for I in 1..N => MM2h(I)(1..H)));

      -- (MX * MRн)
      MXMRh := Multiply_Matrices(MX, (for I in 1..N => MR2h(I)(1..H)));

      -- D * (MX * MRн)
      DMXMRh := Multiply_Vector_Matrix(D, MXMRh);

      -- Aн = a * (Z * MMн) + D * (MX * MRн)
      A2h(1..H) := Calculate_Ah(a, ZMMh, DMXMRh);

      -- Прийняти дані від задачі T7: Aн
      accept Ah_fromT7 (Ah_IN : in Vector_H) do
         A2h(1+H..2*H) := Ah_IN;
      end Ah_fromT7;

      -- Передати в задачу T3 дані: A2н
      T3.A2h_fromT5(A2h);

      Put_Line("Task T5 is finished");
   end T5;

---------------------------------------------------------------------

   task body T6 is
      a6, a5, a4, a8: Integer;
      a, Count: Integer := 0;
      Z, D: Vector_N;
      B2h, C2h, A2h: Vector_2H;
      ZMMh, DMXMRh: Vector_H;
      MX: Matrix_N;
      MM2h, MR2h: Matrix_2H;
      MXMRh: Matrix_H;
      Get_a5_fromT5, Get_a4_fromT4, Get_a8_fromT8: Boolean := False;
   begin
      Put_Line("Task T6 is started");

      -- Прийняти дані від задачі T4: B2н, MX, MM2н, Z, D, C2н, MR2н
      accept B2hMXMM2hZDC2hMR2h_fromT4 (
         B2h_IN : in Vector_2H;
         MX_IN : in Matrix_N;
         MM2h_IN : in Matrix_2H;
         Z_IN : in Vector_N;
         D_IN : in Vector_N;
         C2h_IN : in Vector_2H;
         MR2h_IN : in Matrix_2H
      ) do
         B2h := B2h_IN;
         MX := MX_IN;
         MM2h := MM2h_IN;
         Z := Z_IN;
         D := D_IN;
         C2h := C2h_In;
         MR2h := MR2h_IN;
      end B2hMXMM2hZDC2hMR2h_fromT4;

      -- Передати в задачу T8 дані: Bн, MX, MMн, Z, D, Cн, MRн
      T8.BhMXMMhZDChMRh_fromT6(
         B2h(1+H..2*H),
         MX,
         (for I in 1..N => MM2h(I)(1+H..2*H)),
         Z,
         D,
         C2h(1+H..2*H),
         (for I in 1..N => MR2h(I)(1+H..2*H))
      );

      -- Обчислення1 а6 = (Bн * Cн)
      a6 := Scalar_Product(B2h(1..H), C2h(1..H));

      -- Обчислення2 a = a + a6
      a := a + a6;

      -- Прийняти дані від задачі T5: a5
      -- Обчислення2 a = a + a5
      -- Прийняти дані від задачі T4: a4
      -- Обчислення2 a = a + a4
      -- Прийняти дані від задачі T8: a8
      -- Обчислення2 a = a + a8
      while not (Get_a5_fromT5 and Get_a4_fromT4 and Get_a8_fromT8) loop
         select
            -- Прийняти дані від задачі T5: a5
            accept a5_fromT5 (a5_IN : in Integer) do
               a5 := a5_IN;
            end a5_fromT5;

            -- Обчислення2 a = a + a5
            a := a + a5;

            Get_a5_fromT5 := True;
         or
            -- Прийняти дані від задачі T4: a4
            accept a4_fromT4 (a4_IN : in Integer) do
               a4 := a4_IN;
            end a4_fromT4;

            -- Обчислення2 a = a + a4
            a := a + a4;

            Get_a4_fromT4 := True;
         or
            -- Прийняти дані від задачі T8: a8
            accept a8_fromT8 (a8_IN : in Integer) do
               a8 := a8_IN;
            end a8_fromT8;

            -- Обчислення2 a = a + a8
            a := a + a8;

            Get_a8_fromT8 := True;
         end select;
      end loop;

      -- Передати в задачу T4 дані: a
      -- Передати в задачу T5 дані: a
      -- Передати в задачу T8 дані: a
      while Count < 3 loop
         accept a_fromT6 (a_OUT : out Integer) do
            a_OUT := a;
         end a_fromT6;

         Count := Count + 1;
      end loop;

      -- Обчислення3 Aн = a * (Z * MMн) + D * (MX * MRн)
      -- (Z * MMн)
      ZMMh := Multiply_Vector_Matrix(Z, (for I in 1..N => MM2h(I)(1..H)));

      -- (MX * MRн)
      MXMRh := Multiply_Matrices(MX, (for I in 1..N => MR2h(I)(1..H)));

      -- D * (MX * MRн)
      DMXMRh := Multiply_Vector_Matrix(D, MXMRh);

      -- Aн = a * (Z * MMн) + D * (MX * MRн)
      A2h(1..H) := Calculate_Ah(a, ZMMh, DMXMRh);

      -- Прийняти дані від задачі T8: Aн
      accept Ah_fromT8 (Ah_IN : in Vector_H) do
         A2h(1+H..2*H) := Ah_IN;
      end Ah_fromT8;

      -- Передати в задачу T4 дані: A2н
      T4.A2h_fromT6(A2h);

      Put_Line("Task T6 is finished");
   end T6;

---------------------------------------------------------------------

   task body T7 is
      a7, a: Integer;
      Z, D: Vector_N;
      Bh, Ch, ZMMh, DMXMRh, Ah: Vector_H;
      MX: Matrix_N;
      MMh, MRh, MXMRh: Matrix_H;
   begin
      Put_Line("Task T7 is started");

      -- Прийняти дані від задачі T5: Bн, MX, MMн, Z, D, Cн, MRн
      accept BhMXMMhZDChMRh_fromT5 (
         Bh_IN : in Vector_H;
         MX_IN : in Matrix_N;
         MMh_IN : in Matrix_H;
         Z_IN : in Vector_N;
         D_IN : in Vector_N;
         Ch_IN : in Vector_H;
         MRh_IN : in Matrix_H
      ) do
         Bh := Bh_IN;
         MX := MX_IN;
         MMh := MMh_IN;
         Z := Z_IN;
         D := D_IN;
         Ch := Ch_In;
         MRh := MRh_IN;
      end BhMXMMhZDChMRh_fromT5;

      -- Обчислення1 а7 = (Bн * Cн)
      a7 := Scalar_Product(Bh, Ch);

      -- Передати в задачу T8 дані: a7
      T8.a7_fromT7(a7);

      -- Прийняти дані від задачі T8: a
      accept a_fromT8 (a_IN : in Integer) do
         a := a_IN;
      end a_fromT8;

      -- Обчислення3 Aн = a * (Z * MMн) + D * (MX * MRн)
      -- (Z * MMн)
      ZMMh := Multiply_Vector_Matrix(Z, MMh);

      -- (MX * MRн)
      MXMRh := Multiply_Matrices(MX, MRh);

      -- D * (MX * MRн)
      DMXMRh := Multiply_Vector_Matrix(D, MXMRh);

      -- Aн = a * (Z * MMн) + D * (MX * MRн)
      Ah := Calculate_Ah(a, ZMMh, DMXMRh);

      -- Передати в задачу T5 дані: Aн
      T5.Ah_fromT7(Ah);

      Put_Line("Task T7 is finished");
   end T7;

---------------------------------------------------------------------

   task body T8 is
      a8, a7, a: Integer;
      Z, D: Vector_N;
      Bh, Ch, ZMMh, DMXMRh, Ah: Vector_H;
      MX: Matrix_N;
      MMh, MRh, MXMRh: Matrix_H;
   begin
      Put_Line("Task T8 is started");

      -- Прийняти дані від задачі T6: Bн, MX, MMн, Z, D, Cн, MRн
      accept BhMXMMhZDChMRh_fromT6 (
         Bh_IN : in Vector_H;
         MX_IN : in Matrix_N;
         MMh_IN : in Matrix_H;
         Z_IN : in Vector_N;
         D_IN : in Vector_N;
         Ch_IN : in Vector_H;
         MRh_IN : in Matrix_H
      ) do
         Bh := Bh_IN;
         MX := MX_IN;
         MMh := MMh_IN;
         Z := Z_IN;
         D := D_IN;
         Ch := Ch_In;
         MRh := MRh_IN;
      end BhMXMMhZDChMRh_fromT6;

      -- Обчислення1 а8 = (Bн * Cн)
      a8 := Scalar_Product(Bh, Ch);

      -- Прийняти дані від задачі T7: a7
      accept a7_fromT7 (a7_IN : in Integer) do
         a7 := a7_IN;
      end a7_fromT7;

      -- Обчислення2 a8 = a8 + a7
      a8 := a8 + a7;

      -- Передати в задачу T6 дані: a8
      T6.a8_fromT8(a8);

      -- Прийняти дані від задачі T6: a
      T6.a_fromT6(a);

      -- Передати в задачу T7 дані: a
      T7.a_fromT8(a);

      -- Обчислення3 Aн = a * (Z * MMн) + D * (MX * MRн)
      -- (Z * MMн)
      ZMMh := Multiply_Vector_Matrix(Z, MMh);

      -- (MX * MRн)
      MXMRh := Multiply_Matrices(MX, MRh);

      -- D * (MX * MRн)
      DMXMRh := Multiply_Vector_Matrix(D, MXMRh);

      -- Aн = a * (Z * MMн) + D * (MX * MRн)
      Ah := Calculate_Ah(a, ZMMh, DMXMRh);

      -- Передати в задачу T6 дані: Aн
      T6.Ah_fromT8(Ah);

      Put_Line("Task T8 is finished");
   end T8;

---------------------------------------------------------------------

begin
   Start_Time := Clock;
   Put_Line("Lab5 is started");
end Lab5;
