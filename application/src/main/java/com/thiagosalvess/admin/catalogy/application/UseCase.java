package com.thiagosalvess.admin.catalogy.application;

public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN anId);
}