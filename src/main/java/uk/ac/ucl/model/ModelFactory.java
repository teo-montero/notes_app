package uk.ac.ucl.model;

import java.io.File;
import java.io.IOException;
public class ModelFactory
{
  private static Model model;

  public static Model getModel()
  {
    if (model == null)
    {
      model = new Model();
      model.initialiseModel();
    }
    return model;
  }

  public static void shutdown() {
    model.shutdown();
  }
}
