package org.jacpfx.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by amo on 26.09.14.
 */
@Configuration
@ComponentScan(basePackages = {"org.jacpfx.services","org.jacpfx.repository"})
public class SpringConfiguration {
}
